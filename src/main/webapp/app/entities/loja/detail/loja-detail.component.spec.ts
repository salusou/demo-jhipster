import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LojaDetailComponent } from './loja-detail.component';

describe('Component Tests', () => {
  describe('Loja Management Detail Component', () => {
    let comp: LojaDetailComponent;
    let fixture: ComponentFixture<LojaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LojaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ loja: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LojaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LojaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load loja on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.loja).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
