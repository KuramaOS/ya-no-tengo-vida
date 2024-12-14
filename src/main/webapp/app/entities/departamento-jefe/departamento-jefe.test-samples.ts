import { IDepartamentoJefe, NewDepartamentoJefe } from './departamento-jefe.model';

export const sampleWithRequiredData: IDepartamentoJefe = {
  id: 9821,
  idDepartamento: 'pfft quantify above',
  idJefe: 'disclosure',
};

export const sampleWithPartialData: IDepartamentoJefe = {
  id: 16309,
  idDepartamento: 'what since careless',
  idJefe: 'hmph',
};

export const sampleWithFullData: IDepartamentoJefe = {
  id: 18836,
  idDepartamento: 'however',
  idJefe: 'facilitate what',
};

export const sampleWithNewData: NewDepartamentoJefe = {
  idDepartamento: 'extremely',
  idJefe: 'sans potentially smug',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
