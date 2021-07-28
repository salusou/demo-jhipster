import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILoja, Loja } from '../loja.model';
import { LojaService } from '../service/loja.service';
import { ICompra } from 'app/entities/compra/compra.model';
import { CompraService } from 'app/entities/compra/service/compra.service';

@Component({
  selector: 'jhi-loja-update',
  templateUrl: './loja-update.component.html',
})
export class LojaUpdateComponent implements OnInit {
  isSaving = false;

  comprasSharedCollection: ICompra[] = [];

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    endereco: [],
    telefone: [null, [Validators.required, Validators.maxLength(11)]],
    compra: [],
  });

  constructor(
    protected lojaService: LojaService,
    protected compraService: CompraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ loja }) => {
      this.updateForm(loja);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const loja = this.createFromForm();
    if (loja.id !== undefined) {
      this.subscribeToSaveResponse(this.lojaService.update(loja));
    } else {
      this.subscribeToSaveResponse(this.lojaService.create(loja));
    }
  }

  trackCompraById(index: number, item: ICompra): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILoja>>): void {
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

  protected updateForm(loja: ILoja): void {
    this.editForm.patchValue({
      id: loja.id,
      nome: loja.nome,
      endereco: loja.endereco,
      telefone: loja.telefone,
      compra: loja.compra,
    });

    this.comprasSharedCollection = this.compraService.addCompraToCollectionIfMissing(this.comprasSharedCollection, loja.compra);
  }

  protected loadRelationshipsOptions(): void {
    this.compraService
      .query()
      .pipe(map((res: HttpResponse<ICompra[]>) => res.body ?? []))
      .pipe(map((compras: ICompra[]) => this.compraService.addCompraToCollectionIfMissing(compras, this.editForm.get('compra')!.value)))
      .subscribe((compras: ICompra[]) => (this.comprasSharedCollection = compras));
  }

  protected createFromForm(): ILoja {
    return {
      ...new Loja(),
      id: this.editForm.get(['id'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      endereco: this.editForm.get(['endereco'])!.value,
      telefone: this.editForm.get(['telefone'])!.value,
      compra: this.editForm.get(['compra'])!.value,
    };
  }
}
