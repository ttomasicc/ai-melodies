import {AlbumResponse} from './album.response';
import {HrefLink} from './href-link';

export interface AlbumsResponse {
    _embedded?: { albums: AlbumResponse[] }
    _links: { self: HrefLink }
}