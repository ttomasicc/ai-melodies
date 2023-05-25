import {Pageable} from './pageable';
import {AlbumsResponse} from './albums.response';
import {HrefLink} from './href-link';

export interface AlbumsPageableResponse extends AlbumsResponse {
    _links: {
        first?: HrefLink
        self: HrefLink
        next?: HrefLink
        last?: HrefLink
    }
    page: Pageable
}