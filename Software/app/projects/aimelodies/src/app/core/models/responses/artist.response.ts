import {HrefLink} from './href-link';
import {Role} from '../domain/role';

export interface ArtistResponse {
    id: number
    username: string
    email: string
    firstName?: string
    lastName?: string
    bio?: string
    image?: string
    dateCreated: Date
    role: Role
    _links: {
        self: HrefLink
        albums: HrefLink
        token?: HrefLink
        profile?: HrefLink
        logout?: HrefLink
    }
}