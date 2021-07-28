import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILoja, Loja } from '../loja.model';

import { LojaService } from './loja.service';

describe('Service Tests', () => {
  describe('Loja Service', () => {
    let service: LojaService;
    let httpMock: HttpTestingController;
    let elemDefault: ILoja;
    let expectedResult: ILoja | ILoja[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LojaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nome: 'AAAAAAA',
        endereco: 'AAAAAAA',
        telefone: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Loja', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Loja()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Loja', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            endereco: 'BBBBBB',
            telefone: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Loja', () => {
        const patchObject = Object.assign(
          {
            telefone: 'BBBBBB',
          },
          new Loja()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Loja', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nome: 'BBBBBB',
            endereco: 'BBBBBB',
            telefone: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Loja', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLojaToCollectionIfMissing', () => {
        it('should add a Loja to an empty array', () => {
          const loja: ILoja = { id: 123 };
          expectedResult = service.addLojaToCollectionIfMissing([], loja);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(loja);
        });

        it('should not add a Loja to an array that contains it', () => {
          const loja: ILoja = { id: 123 };
          const lojaCollection: ILoja[] = [
            {
              ...loja,
            },
            { id: 456 },
          ];
          expectedResult = service.addLojaToCollectionIfMissing(lojaCollection, loja);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Loja to an array that doesn't contain it", () => {
          const loja: ILoja = { id: 123 };
          const lojaCollection: ILoja[] = [{ id: 456 }];
          expectedResult = service.addLojaToCollectionIfMissing(lojaCollection, loja);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(loja);
        });

        it('should add only unique Loja to an array', () => {
          const lojaArray: ILoja[] = [{ id: 123 }, { id: 456 }, { id: 98289 }];
          const lojaCollection: ILoja[] = [{ id: 123 }];
          expectedResult = service.addLojaToCollectionIfMissing(lojaCollection, ...lojaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const loja: ILoja = { id: 123 };
          const loja2: ILoja = { id: 456 };
          expectedResult = service.addLojaToCollectionIfMissing([], loja, loja2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(loja);
          expect(expectedResult).toContain(loja2);
        });

        it('should accept null and undefined values', () => {
          const loja: ILoja = { id: 123 };
          expectedResult = service.addLojaToCollectionIfMissing([], null, loja, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(loja);
        });

        it('should return initial array if no Loja is added', () => {
          const lojaCollection: ILoja[] = [{ id: 123 }];
          expectedResult = service.addLojaToCollectionIfMissing(lojaCollection, undefined, null);
          expect(expectedResult).toEqual(lojaCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
