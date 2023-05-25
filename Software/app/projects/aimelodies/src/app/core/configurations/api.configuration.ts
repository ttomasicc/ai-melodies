import {environment} from '../../../environments/environment';

export const apiDomain: string = `http://${environment.api.host}:${environment.api.port}${environment.api.baseUri}`;
export const apiAlbums: string = `http://${environment.api.host}:${environment.api.port}${environment.api.resources.albums}`;
export const apiMelodies: string = `http://${environment.api.host}:${environment.api.port}${environment.api.resources.melodies}`;
export const apiArtists: string = `http://${environment.api.host}:${environment.api.port}${environment.api.resources.artists}`;