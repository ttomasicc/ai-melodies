import {LoginRequest} from './login.request';

export interface RegistrationRequest extends LoginRequest {
    email: string
}