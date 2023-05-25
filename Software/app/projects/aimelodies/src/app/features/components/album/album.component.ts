import {Component, Input} from '@angular/core';
import {Album} from '../../../core/models/domain/album';
import {Router} from '@angular/router';

@Component({
    selector: 'app-album[album]',
    templateUrl: './album.component.html',
    styleUrls: ['./album.component.sass']
})
export class AlbumComponent {
    @Input() album!: Album;
    @Input() controls: boolean = false;

    constructor(private readonly _router: Router) {
    }

    openAlbum = async (): Promise<void> => {
        await this._router.navigate(
            ['/albums'],
            {state: {albumHref: this.album._links.self.href}}
        );
    }
}