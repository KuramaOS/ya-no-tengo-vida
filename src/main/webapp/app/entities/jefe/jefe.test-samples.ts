import { IJefe, NewJefe } from './jefe.model';

export const sampleWithRequiredData: IJefe = {
  id: 14415,
  nombre: 'gigantic yahoo mature',
  telefono: 'instead worthy',
};

export const sampleWithPartialData: IJefe = {
  id: 6540,
  nombre: 'incline which',
  telefono: 'terrible',
};

export const sampleWithFullData: IJefe = {
  id: 9357,
  nombre: 'wrathful whenever',
  telefono: 'oh till hamburger',
};

export const sampleWithNewData: NewJefe = {
  nombre: 'excellent since',
  telefono: 'outlaw',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
