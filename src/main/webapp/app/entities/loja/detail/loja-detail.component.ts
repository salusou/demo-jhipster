import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILoja } from '../loja.model';

@Component({
  selector: 'jhi-loja-detail',
  templateUrl: './loja-detail.component.html',
})
export class LojaDetailComponent implements OnInit {
  loja: ILoja | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ loja }) => {
      this.loja = loja;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
