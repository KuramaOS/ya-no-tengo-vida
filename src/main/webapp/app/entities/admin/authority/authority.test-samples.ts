import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '0605aee1-627c-4735-ba66-205a3f7c3732',
};

export const sampleWithPartialData: IAuthority = {
  name: '07177c3e-1da6-4564-b2aa-5e822f609554',
};

export const sampleWithFullData: IAuthority = {
  name: 'c775a6f8-b52b-46d7-be4e-45a81e9ee993',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
