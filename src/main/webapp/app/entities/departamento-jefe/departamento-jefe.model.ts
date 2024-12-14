import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { IJefe } from 'app/entities/jefe/jefe.model';

export interface IDepartamentoJefe {
  id: number;
  idDepartamento?: string | null;
  idJefe?: string | null;
  departamento?: IDepartamento | null;
  jefe?: IJefe | null;
}

export type NewDepartamentoJefe = Omit<IDepartamentoJefe, 'id'> & { id: null };
