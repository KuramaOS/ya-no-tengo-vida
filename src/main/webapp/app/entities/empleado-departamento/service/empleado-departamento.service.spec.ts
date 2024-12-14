import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IEmpleadoDepartamento } from '../empleado-departamento.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../empleado-departamento.test-samples';

import { EmpleadoDepartamentoService } from './empleado-departamento.service';

const requireRestSample: IEmpleadoDepartamento = {
  ...sampleWithRequiredData,
};

describe('EmpleadoDepartamento Service', () => {
  let service: EmpleadoDepartamentoService;
  let httpMock: HttpTestingController;
  let expectedResult: IEmpleadoDepartamento | IEmpleadoDepartamento[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EmpleadoDepartamentoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a EmpleadoDepartamento', () => {
      const empleadoDepartamento = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(empleadoDepartamento).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EmpleadoDepartamento', () => {
      const empleadoDepartamento = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(empleadoDepartamento).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EmpleadoDepartamento', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EmpleadoDepartamento', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EmpleadoDepartamento', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEmpleadoDepartamentoToCollectionIfMissing', () => {
      it('should add a EmpleadoDepartamento to an empty array', () => {
        const empleadoDepartamento: IEmpleadoDepartamento = sampleWithRequiredData;
        expectedResult = service.addEmpleadoDepartamentoToCollectionIfMissing([], empleadoDepartamento);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(empleadoDepartamento);
      });

      it('should not add a EmpleadoDepartamento to an array that contains it', () => {
        const empleadoDepartamento: IEmpleadoDepartamento = sampleWithRequiredData;
        const empleadoDepartamentoCollection: IEmpleadoDepartamento[] = [
          {
            ...empleadoDepartamento,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEmpleadoDepartamentoToCollectionIfMissing(empleadoDepartamentoCollection, empleadoDepartamento);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EmpleadoDepartamento to an array that doesn't contain it", () => {
        const empleadoDepartamento: IEmpleadoDepartamento = sampleWithRequiredData;
        const empleadoDepartamentoCollection: IEmpleadoDepartamento[] = [sampleWithPartialData];
        expectedResult = service.addEmpleadoDepartamentoToCollectionIfMissing(empleadoDepartamentoCollection, empleadoDepartamento);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(empleadoDepartamento);
      });

      it('should add only unique EmpleadoDepartamento to an array', () => {
        const empleadoDepartamentoArray: IEmpleadoDepartamento[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const empleadoDepartamentoCollection: IEmpleadoDepartamento[] = [sampleWithRequiredData];
        expectedResult = service.addEmpleadoDepartamentoToCollectionIfMissing(empleadoDepartamentoCollection, ...empleadoDepartamentoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const empleadoDepartamento: IEmpleadoDepartamento = sampleWithRequiredData;
        const empleadoDepartamento2: IEmpleadoDepartamento = sampleWithPartialData;
        expectedResult = service.addEmpleadoDepartamentoToCollectionIfMissing([], empleadoDepartamento, empleadoDepartamento2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(empleadoDepartamento);
        expect(expectedResult).toContain(empleadoDepartamento2);
      });

      it('should accept null and undefined values', () => {
        const empleadoDepartamento: IEmpleadoDepartamento = sampleWithRequiredData;
        expectedResult = service.addEmpleadoDepartamentoToCollectionIfMissing([], null, empleadoDepartamento, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(empleadoDepartamento);
      });

      it('should return initial array if no EmpleadoDepartamento is added', () => {
        const empleadoDepartamentoCollection: IEmpleadoDepartamento[] = [sampleWithRequiredData];
        expectedResult = service.addEmpleadoDepartamentoToCollectionIfMissing(empleadoDepartamentoCollection, undefined, null);
        expect(expectedResult).toEqual(empleadoDepartamentoCollection);
      });
    });

    describe('compareEmpleadoDepartamento', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEmpleadoDepartamento(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEmpleadoDepartamento(entity1, entity2);
        const compareResult2 = service.compareEmpleadoDepartamento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEmpleadoDepartamento(entity1, entity2);
        const compareResult2 = service.compareEmpleadoDepartamento(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEmpleadoDepartamento(entity1, entity2);
        const compareResult2 = service.compareEmpleadoDepartamento(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
