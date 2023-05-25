import {HrefLink} from './href-link';

export interface AlbumResponse {
    id: number,
    title: string,
    description?: string,
    image?: string,
    dateCreated: Date,
    _links: { self: HrefLink, melodies: HrefLink, artist: HrefLink }
}