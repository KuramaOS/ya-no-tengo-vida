import { IEmpleado, NewEmpleado } from './empleado.model';

export const sampleWithRequiredData: IEmpleado = {
  id: 6119,
  nombre: 'anneal',
  apellido: 'catch',
  correo: 'generously',
};

export const sampleWithPartialData: IEmpleado = {
  id: 6689,
  nombre: 'because silently pecan',
  apellido: 'preregister',
  telefono: 'finally far-off yuck',
  correo: 'only overdub',
};

export const sampleWithFullData: IEmpleado = {
  id: 6143,
  nombre: 'trash',
  apellido: 'forenenst',
  telefono: 'who along',
  correo: 'print charm',
};

export const sampleWithNewData: NewEmpleado = {
  nombre: 'of spice soup',
  apellido: 'qua',
  correo: 'punctual knowingly gah',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
