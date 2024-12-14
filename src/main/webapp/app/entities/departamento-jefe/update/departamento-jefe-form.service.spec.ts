import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../departamento-jefe.test-samples';

import { DepartamentoJefeFormService } from './departamento-jefe-form.service';

describe('DepartamentoJefe Form Service', () => {
  let service: DepartamentoJefeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DepartamentoJefeFormService);
  });

  describe('Service methods', () => {
    describe('createDepartamentoJefeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDepartamentoJefeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idDepartamento: expect.any(Object),
            idJefe: expect.any(Object),
            departamento: expect.any(Object),
            jefe: expect.any(Object),
          }),
        );
      });

      it('passing IDepartamentoJefe should create a new form with FormGroup', () => {
        const formGroup = service.createDepartamentoJefeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            idDepartamento: expect.any(Object),
            idJefe: expect.any(Object),
            departamento: expect.any(Object),
            jefe: expect.any(Object),
          }),
        );
      });
    });

    describe('getDepartamentoJefe', () => {
      it('should return NewDepartamentoJefe for default DepartamentoJefe initial value', () => {
        const formGroup = service.createDepartamentoJefeFormGroup(sampleWithNewData);

        const departamentoJefe = service.getDepartamentoJefe(formGroup) as any;

        expect(departamentoJefe).toMatchObject(sampleWithNewData);
      });

      it('should return NewDepartamentoJefe for empty DepartamentoJefe initial value', () => {
        const formGroup = service.createDepartamentoJefeFormGroup();

        const departamentoJefe = service.getDepartamentoJefe(formGroup) as any;

        expect(departamentoJefe).toMatchObject({});
      });

      it('should return IDepartamentoJefe', () => {
        const formGroup = service.createDepartamentoJefeFormGroup(sampleWithRequiredData);

        const departamentoJefe = service.getDepartamentoJefe(formGroup) as any;

        expect(departamentoJefe).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDepartamentoJefe should not enable id FormControl', () => {
        const formGroup = service.createDepartamentoJefeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDepartamentoJefe should disable id FormControl', () => {
        const formGroup = service.createDepartamentoJefeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
