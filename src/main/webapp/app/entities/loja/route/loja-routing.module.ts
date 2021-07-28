import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LojaComponent } from '../list/loja.component';
import { LojaDetailComponent } from '../detail/loja-detail.component';
import { LojaUpdateComponent } from '../update/loja-update.component';
import { LojaRoutingResolveService } from './loja-routing-resolve.service';

const lojaRoute: Routes = [
  {
    path: '',
    component: LojaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LojaDetailComponent,
    resolve: {
      loja: LojaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LojaUpdateComponent,
    resolve: {
      loja: LojaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LojaUpdateComponent,
    resolve: {
      loja: LojaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lojaRoute)],
  exports: [RouterModule],
})
export class LojaRoutingModule {}
