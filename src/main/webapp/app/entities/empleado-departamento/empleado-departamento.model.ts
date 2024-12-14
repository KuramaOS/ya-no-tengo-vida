import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { IDepartamento } from 'app/entities/departamento/departamento.model';

export interface IEmpleadoDepartamento {
  id: number;
  idEmpleado?: string | null;
  idDepartamento?: string | null;
  empleado?: IEmpleado | null;
  departamento?: IDepartamento | null;
}

export type NewEmpleadoDepartamento = Omit<IEmpleadoDepartamento, 'id'> & { id: null };
