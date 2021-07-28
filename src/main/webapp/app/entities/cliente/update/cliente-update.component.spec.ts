jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClienteService } from '../service/cliente.service';
import { ICliente, Cliente } from '../cliente.model';
import { ICompra } from 'app/entities/compra/compra.model';
import { CompraService } from 'app/entities/compra/service/compra.service';

import { ClienteUpdateComponent } from './cliente-update.component';

describe('Component Tests', () => {
  describe('Cliente Management Update Component', () => {
    let comp: ClienteUpdateComponent;
    let fixture: ComponentFixture<ClienteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let clienteService: ClienteService;
    let compraService: CompraService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClienteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClienteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClienteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      clienteService = TestBed.inject(ClienteService);
      compraService = TestBed.inject(CompraService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Compra query and add missing value', () => {
        const cliente: ICliente = { id: 456 };
        const compra: ICompra = { id: 80246 };
        cliente.compra = compra;

        const compraCollection: ICompra[] = [{ id: 58219 }];
        jest.spyOn(compraService, 'query').mockReturnValue(of(new HttpResponse({ body: compraCollection })));
        const additionalCompras = [compra];
        const expectedCollection: ICompra[] = [...additionalCompras, ...compraCollection];
        jest.spyOn(compraService, 'addCompraToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ cliente });
        comp.ngOnInit();

        expect(compraService.query).toHaveBeenCalled();
        expect(compraService.addCompraToCollectionIfMissing).toHaveBeenCalledWith(compraCollection, ...additionalCompras);
        expect(comp.comprasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const cliente: ICliente = { id: 456 };
        const compra: ICompra = { id: 47956 };
        cliente.compra = compra;

        activatedRoute.data = of({ cliente });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(cliente));
        expect(comp.comprasSharedCollection).toContain(compra);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Cliente>>();
        const cliente = { id: 123 };
        jest.spyOn(clienteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cliente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cliente }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(clienteService.update).toHaveBeenCalledWith(cliente);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Cliente>>();
        const cliente = new Cliente();
        jest.spyOn(clienteService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cliente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: cliente }));
        saveSubject.complete();

        // THEN
        expect(clienteService.create).toHaveBeenCalledWith(cliente);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Cliente>>();
        const cliente = { id: 123 };
        jest.spyOn(clienteService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ cliente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(clienteService.update).toHaveBeenCalledWith(cliente);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCompraById', () => {
        it('Should return tracked Compra primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCompraById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
