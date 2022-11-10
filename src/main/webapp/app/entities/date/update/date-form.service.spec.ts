import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../date.test-samples';

import { DateFormService } from './date-form.service';

describe('Date Form Service', () => {
  let service: DateFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DateFormService);
  });

  describe('Service methods', () => {
    describe('createDateFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDateFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            day: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
          })
        );
      });

      it('passing IDate should create a new form with FormGroup', () => {
        const formGroup = service.createDateFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            day: expect.any(Object),
            month: expect.any(Object),
            year: expect.any(Object),
          })
        );
      });
    });

    describe('getDate', () => {
      it('should return NewDate for default Date initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDateFormGroup(sampleWithNewData);

        const date = service.getDate(formGroup) as any;

        expect(date).toMatchObject(sampleWithNewData);
      });

      it('should return NewDate for empty Date initial value', () => {
        const formGroup = service.createDateFormGroup();

        const date = service.getDate(formGroup) as any;

        expect(date).toMatchObject({});
      });

      it('should return IDate', () => {
        const formGroup = service.createDateFormGroup(sampleWithRequiredData);

        const date = service.getDate(formGroup) as any;

        expect(date).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDate should not enable id FormControl', () => {
        const formGroup = service.createDateFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDate should disable id FormControl', () => {
        const formGroup = service.createDateFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
