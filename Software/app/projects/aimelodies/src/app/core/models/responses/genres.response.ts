import {GenreResponse} from './genre.response';
import {HrefLink} from './href-link';

export interface GenresResponse {
    _embedded?: { genres: GenreResponse[] }
    _links: { self: HrefLink }
}