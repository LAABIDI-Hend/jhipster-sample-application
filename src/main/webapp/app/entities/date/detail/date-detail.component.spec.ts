import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DateDetailComponent } from './date-detail.component';

describe('Date Management Detail Component', () => {
  let comp: DateDetailComponent;
  let fixture: ComponentFixture<DateDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DateDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ date: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DateDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DateDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load date on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.date).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
