import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { EmpleadoService } from 'app/entities/empleado/service/empleado.service';
import { IDepartamento } from 'app/entities/departamento/departamento.model';
import { DepartamentoService } from 'app/entities/departamento/service/departamento.service';
import { IEmpleadoDepartamento } from '../empleado-departamento.model';
import { EmpleadoDepartamentoService } from '../service/empleado-departamento.service';
import { EmpleadoDepartamentoFormService } from './empleado-departamento-form.service';

import { EmpleadoDepartamentoUpdateComponent } from './empleado-departamento-update.component';

describe('EmpleadoDepartamento Management Update Component', () => {
  let comp: EmpleadoDepartamentoUpdateComponent;
  let fixture: ComponentFixture<EmpleadoDepartamentoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let empleadoDepartamentoFormService: EmpleadoDepartamentoFormService;
  let empleadoDepartamentoService: EmpleadoDepartamentoService;
  let empleadoService: EmpleadoService;
  let departamentoService: DepartamentoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EmpleadoDepartamentoUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EmpleadoDepartamentoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmpleadoDepartamentoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    empleadoDepartamentoFormService = TestBed.inject(EmpleadoDepartamentoFormService);
    empleadoDepartamentoService = TestBed.inject(EmpleadoDepartamentoService);
    empleadoService = TestBed.inject(EmpleadoService);
    departamentoService = TestBed.inject(DepartamentoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Empleado query and add missing value', () => {
      const empleadoDepartamento: IEmpleadoDepartamento = { id: 456 };
      const empleado: IEmpleado = { id: 25968 };
      empleadoDepartamento.empleado = empleado;

      const empleadoCollection: IEmpleado[] = [{ id: 20266 }];
      jest.spyOn(empleadoService, 'query').mockReturnValue(of(new HttpResponse({ body: empleadoCollection })));
      const additionalEmpleados = [empleado];
      const expectedCollection: IEmpleado[] = [...additionalEmpleados, ...empleadoCollection];
      jest.spyOn(empleadoService, 'addEmpleadoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empleadoDepartamento });
      comp.ngOnInit();

      expect(empleadoService.query).toHaveBeenCalled();
      expect(empleadoService.addEmpleadoToCollectionIfMissing).toHaveBeenCalledWith(
        empleadoCollection,
        ...additionalEmpleados.map(expect.objectContaining),
      );
      expect(comp.empleadosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Departamento query and add missing value', () => {
      const empleadoDepartamento: IEmpleadoDepartamento = { id: 456 };
      const departamento: IDepartamento = { id: 27173 };
      empleadoDepartamento.departamento = departamento;

      const departamentoCollection: IDepartamento[] = [{ id: 4960 }];
      jest.spyOn(departamentoService, 'query').mockReturnValue(of(new HttpResponse({ body: departamentoCollection })));
      const additionalDepartamentos = [departamento];
      const expectedCollection: IDepartamento[] = [...additionalDepartamentos, ...departamentoCollection];
      jest.spyOn(departamentoService, 'addDepartamentoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ empleadoDepartamento });
      comp.ngOnInit();

      expect(departamentoService.query).toHaveBeenCalled();
      expect(departamentoService.addDepartamentoToCollectionIfMissing).toHaveBeenCalledWith(
        departamentoCollection,
        ...additionalDepartamentos.map(expect.objectContaining),
      );
      expect(comp.departamentosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const empleadoDepartamento: IEmpleadoDepartamento = { id: 456 };
      const empleado: IEmpleado = { id: 22572 };
      empleadoDepartamento.empleado = empleado;
      const departamento: IDepartamento = { id: 12041 };
      empleadoDepartamento.departamento = departamento;

      activatedRoute.data = of({ empleadoDepartamento });
      comp.ngOnInit();

      expect(comp.empleadosSharedCollection).toContain(empleado);
      expect(comp.departamentosSharedCollection).toContain(departamento);
      expect(comp.empleadoDepartamento).toEqual(empleadoDepartamento);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleadoDepartamento>>();
      const empleadoDepartamento = { id: 123 };
      jest.spyOn(empleadoDepartamentoFormService, 'getEmpleadoDepartamento').mockReturnValue(empleadoDepartamento);
      jest.spyOn(empleadoDepartamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleadoDepartamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleadoDepartamento }));
      saveSubject.complete();

      // THEN
      expect(empleadoDepartamentoFormService.getEmpleadoDepartamento).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(empleadoDepartamentoService.update).toHaveBeenCalledWith(expect.objectContaining(empleadoDepartamento));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleadoDepartamento>>();
      const empleadoDepartamento = { id: 123 };
      jest.spyOn(empleadoDepartamentoFormService, 'getEmpleadoDepartamento').mockReturnValue({ id: null });
      jest.spyOn(empleadoDepartamentoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleadoDepartamento: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: empleadoDepartamento }));
      saveSubject.complete();

      // THEN
      expect(empleadoDepartamentoFormService.getEmpleadoDepartamento).toHaveBeenCalled();
      expect(empleadoDepartamentoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmpleadoDepartamento>>();
      const empleadoDepartamento = { id: 123 };
      jest.spyOn(empleadoDepartamentoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ empleadoDepartamento });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(empleadoDepartamentoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmpleado', () => {
      it('Should forward to empleadoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(empleadoService, 'compareEmpleado');
        comp.compareEmpleado(entity, entity2);
        expect(empleadoService.compareEmpleado).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDepartamento', () => {
      it('Should forward to departamentoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departamentoService, 'compareDepartamento');
        comp.compareDepartamento(entity, entity2);
        expect(departamentoService.compareDepartamento).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
