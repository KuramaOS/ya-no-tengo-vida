import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'empleado',
    data: { pageTitle: 'Empleados' },
    loadChildren: () => import('./empleado/empleado.routes'),
  },
  {
    path: 'departamento',
    data: { pageTitle: 'Departamentos' },
    loadChildren: () => import('./departamento/departamento.routes'),
  },
  {
    path: 'departamento-jefe',
    data: { pageTitle: 'DepartamentoJefes' },
    loadChildren: () => import('./departamento-jefe/departamento-jefe.routes'),
  },
  {
    path: 'empleado-departamento',
    data: { pageTitle: 'EmpleadoDepartamentos' },
    loadChildren: () => import('./empleado-departamento/empleado-departamento.routes'),
  },
  {
    path: 'jefe',
    data: { pageTitle: 'Jefes' },
    loadChildren: () => import('./jefe/jefe.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
