import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILoja, getLojaIdentifier } from '../loja.model';

export type EntityResponseType = HttpResponse<ILoja>;
export type EntityArrayResponseType = HttpResponse<ILoja[]>;

@Injectable({ providedIn: 'root' })
export class LojaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lojas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(loja: ILoja): Observable<EntityResponseType> {
    return this.http.post<ILoja>(this.resourceUrl, loja, { observe: 'response' });
  }

  update(loja: ILoja): Observable<EntityResponseType> {
    return this.http.put<ILoja>(`${this.resourceUrl}/${getLojaIdentifier(loja) as number}`, loja, { observe: 'response' });
  }

  partialUpdate(loja: ILoja): Observable<EntityResponseType> {
    return this.http.patch<ILoja>(`${this.resourceUrl}/${getLojaIdentifier(loja) as number}`, loja, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILoja>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILoja[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLojaToCollectionIfMissing(lojaCollection: ILoja[], ...lojasToCheck: (ILoja | null | undefined)[]): ILoja[] {
    const lojas: ILoja[] = lojasToCheck.filter(isPresent);
    if (lojas.length > 0) {
      const lojaCollectionIdentifiers = lojaCollection.map(lojaItem => getLojaIdentifier(lojaItem)!);
      const lojasToAdd = lojas.filter(lojaItem => {
        const lojaIdentifier = getLojaIdentifier(lojaItem);
        if (lojaIdentifier == null || lojaCollectionIdentifiers.includes(lojaIdentifier)) {
          return false;
        }
        lojaCollectionIdentifiers.push(lojaIdentifier);
        return true;
      });
      return [...lojasToAdd, ...lojaCollection];
    }
    return lojaCollection;
  }
}
