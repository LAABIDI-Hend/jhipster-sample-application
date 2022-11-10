import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DateFormService } from './date-form.service';
import { DateService } from '../service/date.service';
import { IDate } from '../date.model';

import { DateUpdateComponent } from './date-update.component';

describe('Date Management Update Component', () => {
  let comp: DateUpdateComponent;
  let fixture: ComponentFixture<DateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dateFormService: DateFormService;
  let dateService: DateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DateUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DateUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dateFormService = TestBed.inject(DateFormService);
    dateService = TestBed.inject(DateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const date: IDate = { id: 456 };

      activatedRoute.data = of({ date });
      comp.ngOnInit();

      expect(comp.date).toEqual(date);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDate>>();
      const date = { id: 123 };
      jest.spyOn(dateFormService, 'getDate').mockReturnValue(date);
      jest.spyOn(dateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ date });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: date }));
      saveSubject.complete();

      // THEN
      expect(dateFormService.getDate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dateService.update).toHaveBeenCalledWith(expect.objectContaining(date));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDate>>();
      const date = { id: 123 };
      jest.spyOn(dateFormService, 'getDate').mockReturnValue({ id: null });
      jest.spyOn(dateService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ date: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: date }));
      saveSubject.complete();

      // THEN
      expect(dateFormService.getDate).toHaveBeenCalled();
      expect(dateService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDate>>();
      const date = { id: 123 };
      jest.spyOn(dateService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ date });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dateService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
