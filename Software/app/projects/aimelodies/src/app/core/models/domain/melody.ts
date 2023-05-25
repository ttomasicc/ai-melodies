import {MelodyResponse} from '../responses/melody.response';
import {Artist} from './artist';

export interface Melody extends MelodyResponse {
    duration?: number
    artist?: Artist
}