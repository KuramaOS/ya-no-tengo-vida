import { IEmpleadoDepartamento, NewEmpleadoDepartamento } from './empleado-departamento.model';

export const sampleWithRequiredData: IEmpleadoDepartamento = {
  id: 6968,
  idEmpleado: 'ample pointed veg',
  idDepartamento: 'as brr',
};

export const sampleWithPartialData: IEmpleadoDepartamento = {
  id: 28114,
  idEmpleado: 'youthful chiffonier gee',
  idDepartamento: 'huzzah',
};

export const sampleWithFullData: IEmpleadoDepartamento = {
  id: 6021,
  idEmpleado: 'vainly failing pear',
  idDepartamento: 'bicycle smoothly',
};

export const sampleWithNewData: NewEmpleadoDepartamento = {
  idEmpleado: 'because scrap',
  idDepartamento: 'hm kissingly gosh',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
