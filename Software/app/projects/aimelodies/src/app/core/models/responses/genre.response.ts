import {HrefLink} from './href-link';

export interface GenreResponse {
    id: number
    name: string
    _links: { self: HrefLink }
}