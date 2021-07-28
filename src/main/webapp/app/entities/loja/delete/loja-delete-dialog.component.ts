import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILoja } from '../loja.model';
import { LojaService } from '../service/loja.service';

@Component({
  templateUrl: './loja-delete-dialog.component.html',
})
export class LojaDeleteDialogComponent {
  loja?: ILoja;

  constructor(protected lojaService: LojaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lojaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
