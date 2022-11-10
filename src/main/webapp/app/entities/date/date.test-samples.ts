import { IDate, NewDate } from './date.model';

export const sampleWithRequiredData: IDate = {
  id: 26380,
};

export const sampleWithPartialData: IDate = {
  id: 61944,
  day: 'hack',
  month: 'back-end internet',
};

export const sampleWithFullData: IDate = {
  id: 78681,
  day: 'XML viral',
  month: 'Berkshire',
  year: 'copy compress',
};

export const sampleWithNewData: NewDate = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
