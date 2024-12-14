import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../empleado-departamento.test-samples';

import { EmpleadoDepartamentoFormService } from './empleado-departamento-form.service';

describe('EmpleadoDepartamento Form Service', () => {
  let service: EmpleadoDepartamentoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmpleadoDepartamentoFormService);
  });

  describe('Service methods', () => {
    describe('createEmpleadoDepartamentoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmpleadoDepartamentoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idEmpleado: expect.any(Object),
            idDepartamento: expect.any(Object),
            empleado: expect.any(Object),
            departamento: expect.any(Object),
          }),
        );
      });

      it('passing IEmpleadoDepartamento should create a new form with FormGroup', () => {
        const formGroup = service.createEmpleadoDepartamentoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idEmpleado: expect.any(Object),
            idDepartamento: expect.any(Object),
            empleado: expect.any(Object),
            departamento: expect.any(Object),
          }),
        );
      });
    });

    describe('getEmpleadoDepartamento', () => {
      it('should return NewEmpleadoDepartamento for default EmpleadoDepartamento initial value', () => {
        const formGroup = service.createEmpleadoDepartamentoFormGroup(sampleWithNewData);

        const empleadoDepartamento = service.getEmpleadoDepartamento(formGroup) as any;

        expect(empleadoDepartamento).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmpleadoDepartamento for empty EmpleadoDepartamento initial value', () => {
        const formGroup = service.createEmpleadoDepartamentoFormGroup();

        const empleadoDepartamento = service.getEmpleadoDepartamento(formGroup) as any;

        expect(empleadoDepartamento).toMatchObject({});
      });

      it('should return IEmpleadoDepartamento', () => {
        const formGroup = service.createEmpleadoDepartamentoFormGroup(sampleWithRequiredData);

        const empleadoDepartamento = service.getEmpleadoDepartamento(formGroup) as any;

        expect(empleadoDepartamento).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmpleadoDepartamento should not enable id FormControl', () => {
        const formGroup = service.createEmpleadoDepartamentoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmpleadoDepartamento should disable id FormControl', () => {
        const formGroup = service.createEmpleadoDepartamentoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
