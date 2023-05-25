import {Pageable} from './pageable';
import {HrefLink} from './href-link';
import {MelodiesResponse} from './melodies.response';

export interface MelodiesPageableResponse extends MelodiesResponse {
    _links: {
        first?: HrefLink
        self: HrefLink
        next?: HrefLink
        last?: HrefLink
    }
    page: Pageable
}