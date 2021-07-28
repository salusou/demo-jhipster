import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICliente, Cliente } from '../cliente.model';
import { ClienteService } from '../service/cliente.service';
import { ICompra } from 'app/entities/compra/compra.model';
import { CompraService } from 'app/entities/compra/service/compra.service';

@Component({
  selector: 'jhi-cliente-update',
  templateUrl: './cliente-update.component.html',
})
export class ClienteUpdateComponent implements OnInit {
  isSaving = false;

  comprasSharedCollection: ICompra[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    endereco: [],
    telefone: [],
    compra: [],
  });

  constructor(
    protected clienteService: ClienteService,
    protected compraService: CompraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cliente }) => {
      this.updateForm(cliente);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cliente = this.createFromForm();
    if (cliente.id !== undefined) {
      this.subscribeToSaveResponse(this.clienteService.update(cliente));
    } else {
      this.subscribeToSaveResponse(this.clienteService.create(cliente));
    }
  }

  trackCompraById(index: number, item: ICompra): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICliente>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(cliente: ICliente): void {
    this.editForm.patchValue({
      id: cliente.id,
      nome: cliente.nome,
      endereco: cliente.endereco,
      telefone: cliente.telefone,
      compra: cliente.compra,
    });

    this.comprasSharedCollection = this.compraService.addCompraToCollectionIfMissing(this.comprasSharedCollection, cliente.compra);
  }

  protected loadRelationshipsOptions(): void {
    this.compraService
      .query()
      .pipe(map((res: HttpResponse<ICompra[]>) => res.body ?? []))
      .pipe(map((compras: ICompra[]) => this.compraService.addCompraToCollectionIfMissing(compras, this.editForm.get('compra')!.value)))
      .subscribe((compras: ICompra[]) => (this.comprasSharedCollection = compras));
  }

  protected createFromForm(): ICliente {
    return {
      ...new Cliente(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      endereco: this.editForm.get(['endereco'])!.value,
      telefone: this.editForm.get(['telefone'])!.value,
      compra: this.editForm.get(['compra'])!.value,
    };
  }
}
