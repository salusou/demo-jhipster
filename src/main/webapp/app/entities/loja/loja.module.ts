import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LojaComponent } from './list/loja.component';
import { LojaDetailComponent } from './detail/loja-detail.component';
import { LojaUpdateComponent } from './update/loja-update.component';
import { LojaDeleteDialogComponent } from './delete/loja-delete-dialog.component';
import { LojaRoutingModule } from './route/loja-routing.module';

@NgModule({
  imports: [SharedModule, LojaRoutingModule],
  declarations: [LojaComponent, LojaDetailComponent, LojaUpdateComponent, LojaDeleteDialogComponent],
  entryComponents: [LojaDeleteDialogComponent],
})
export class LojaModule {}
