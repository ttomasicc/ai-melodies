import {HrefLink} from './href-link';

export interface JwtResponse {
    token?: string
    _links?: { self: HrefLink, login: HrefLink }
}