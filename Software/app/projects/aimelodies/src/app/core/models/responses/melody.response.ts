import {GenreResponse} from './genre.response';
import {HrefLink} from './href-link';

export interface MelodyResponse {
    id: number
    audio: string
    name: string
    dateCreated: Date
    genre: GenreResponse
    _links: { self: HrefLink, album: HrefLink, artist: HrefLink }
}