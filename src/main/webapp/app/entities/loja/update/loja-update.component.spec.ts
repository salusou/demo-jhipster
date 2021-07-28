jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LojaService } from '../service/loja.service';
import { ILoja, Loja } from '../loja.model';
import { ICompra } from 'app/entities/compra/compra.model';
import { CompraService } from 'app/entities/compra/service/compra.service';

import { LojaUpdateComponent } from './loja-update.component';

describe('Component Tests', () => {
  describe('Loja Management Update Component', () => {
    let comp: LojaUpdateComponent;
    let fixture: ComponentFixture<LojaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let lojaService: LojaService;
    let compraService: CompraService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LojaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LojaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LojaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      lojaService = TestBed.inject(LojaService);
      compraService = TestBed.inject(CompraService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Compra query and add missing value', () => {
        const loja: ILoja = { id: 456 };
        const compra: ICompra = { id: 64 };
        loja.compra = compra;

        const compraCollection: ICompra[] = [{ id: 19185 }];
        jest.spyOn(compraService, 'query').mockReturnValue(of(new HttpResponse({ body: compraCollection })));
        const additionalCompras = [compra];
        const expectedCollection: ICompra[] = [...additionalCompras, ...compraCollection];
        jest.spyOn(compraService, 'addCompraToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ loja });
        comp.ngOnInit();

        expect(compraService.query).toHaveBeenCalled();
        expect(compraService.addCompraToCollectionIfMissing).toHaveBeenCalledWith(compraCollection, ...additionalCompras);
        expect(comp.comprasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const loja: ILoja = { id: 456 };
        const compra: ICompra = { id: 19020 };
        loja.compra = compra;

        activatedRoute.data = of({ loja });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(loja));
        expect(comp.comprasSharedCollection).toContain(compra);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Loja>>();
        const loja = { id: 123 };
        jest.spyOn(lojaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ loja });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: loja }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(lojaService.update).toHaveBeenCalledWith(loja);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Loja>>();
        const loja = new Loja();
        jest.spyOn(lojaService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ loja });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: loja }));
        saveSubject.complete();

        // THEN
        expect(lojaService.create).toHaveBeenCalledWith(loja);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Loja>>();
        const loja = { id: 123 };
        jest.spyOn(lojaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ loja });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(lojaService.update).toHaveBeenCalledWith(loja);
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
