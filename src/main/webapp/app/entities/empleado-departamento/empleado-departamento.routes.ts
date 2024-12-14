import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EmpleadoDepartamentoResolve from './route/empleado-departamento-routing-resolve.service';

const empleadoDepartamentoRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/empleado-departamento.component').then(m => m.EmpleadoDepartamentoComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/empleado-departamento-detail.component').then(m => m.EmpleadoDepartamentoDetailComponent),
    resolve: {
      empleadoDepartamento: EmpleadoDepartamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/empleado-departamento-update.component').then(m => m.EmpleadoDepartamentoUpdateComponent),
    resolve: {
      empleadoDepartamento: EmpleadoDepartamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/empleado-departamento-update.component').then(m => m.EmpleadoDepartamentoUpdateComponent),
    resolve: {
      empleadoDepartamento: EmpleadoDepartamentoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default empleadoDepartamentoRoute;
