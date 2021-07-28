import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILoja, Loja } from '../loja.model';
import { LojaService } from '../service/loja.service';

@Injectable({ providedIn: 'root' })
export class LojaRoutingResolveService implements Resolve<ILoja> {
  constructor(protected service: LojaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILoja> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((loja: HttpResponse<Loja>) => {
          if (loja.body) {
            return of(loja.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Loja());
  }
}
