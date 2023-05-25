import {MelodyResponse} from './melody.response';
import {HrefLink} from './href-link';

export interface MelodiesResponse {
    _embedded?: { melodies: MelodyResponse[] }
    _links: { self: HrefLink}
}