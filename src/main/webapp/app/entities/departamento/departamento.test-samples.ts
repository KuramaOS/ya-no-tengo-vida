import { IDepartamento, NewDepartamento } from './departamento.model';

export const sampleWithRequiredData: IDepartamento = {
  id: 9049,
  nombre: 'reasoning since unibody',
  ubicacion: 'along',
  presupuesto: 'opera truly save',
};

export const sampleWithPartialData: IDepartamento = {
  id: 15584,
  nombre: 'lack thorny',
  ubicacion: 'mechanically mid',
  presupuesto: 'yet',
};

export const sampleWithFullData: IDepartamento = {
  id: 17828,
  nombre: 'meanwhile',
  ubicacion: 'supposing',
  presupuesto: 'reel better ack',
};

export const sampleWithNewData: NewDepartamento = {
  nombre: 'tedious highly',
  ubicacion: 'below extremely hence',
  presupuesto: 'superficial brace important',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
