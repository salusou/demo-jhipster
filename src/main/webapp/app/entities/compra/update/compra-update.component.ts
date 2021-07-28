import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICompra, Compra } from '../compra.model';
import { CompraService } from '../service/compra.service';

@Component({
  selector: 'jhi-compra-update',
  templateUrl: './compra-update.component.html',
})
export class CompraUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomeProduto: [null, [Validators.required]],
    valor: [null, [Validators.required]],
  });

  constructor(protected compraService: CompraService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compra }) => {
      this.updateForm(compra);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compra = this.createFromForm();
    if (compra.id !== undefined) {
      this.subscribeToSaveResponse(this.compraService.update(compra));
    } else {
      this.subscribeToSaveResponse(this.compraService.create(compra));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompra>>): void {
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

  protected updateForm(compra: ICompra): void {
    this.editForm.patchValue({
      id: compra.id,
      nomeProduto: compra.nomeProduto,
      valor: compra.valor,
    });
  }

  protected createFromForm(): ICompra {
    return {
      ...new Compra(),
      id: this.editForm.get(['id'])!.value,
      nomeProduto: this.editForm.get(['nomeProduto'])!.value,
      valor: this.editForm.get(['valor'])!.value,
    };
  }
}
