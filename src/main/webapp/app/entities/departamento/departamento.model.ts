export interface IDepartamento {
  id: number;
  nombre?: string | null;
  ubicacion?: string | null;
  presupuesto?: string | null;
}

export type NewDepartamento = Omit<IDepartamento, 'id'> & { id: null };
