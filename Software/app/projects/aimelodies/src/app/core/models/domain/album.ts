import {AlbumResponse} from '../responses/album.response';
import {Artist} from './artist';
import {Melody} from './melody';

export interface Album extends AlbumResponse {
    artist?: Artist
    melodies?: Melody[]
}