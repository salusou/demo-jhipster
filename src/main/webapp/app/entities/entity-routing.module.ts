import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'loja',
        data: { pageTitle: 'thalissonApp.loja.home.title' },
        loadChildren: () => import('./loja/loja.module').then(m => m.LojaModule),
      },
      {
        path: 'cliente',
        data: { pageTitle: 'thalissonApp.cliente.home.title' },
        loadChildren: () => import('./cliente/cliente.module').then(m => m.ClienteModule),
      },
      {
        path: 'compra',
        data: { pageTitle: 'thalissonApp.compra.home.title' },
        loadChildren: () => import('./compra/compra.module').then(m => m.CompraModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
