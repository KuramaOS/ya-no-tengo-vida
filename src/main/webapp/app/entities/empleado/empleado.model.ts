export interface IEmpleado {
  id: number;
  nombre?: string | null;
  apellido?: string | null;
  telefono?: string | null;
  correo?: string | null;
}

export type NewEmpleado = Omit<IEmpleado, 'id'> & { id: null };
