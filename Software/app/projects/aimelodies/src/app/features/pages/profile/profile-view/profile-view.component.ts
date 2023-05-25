import {Component, OnInit} from '@angular/core';
import {Artist} from '../../../../core/models/domain/artist';
import {ArtistService} from '../../../../core/services/artist.service';

@Component({
    selector: 'app-profile-view',
    templateUrl: './profile-view.component.html',
    styleUrls: ['./profile-view.component.sass']
})
export class ProfileViewComponent implements OnInit {
    artist?: Artist;

    constructor(private readonly _artistService: ArtistService) {
    }

    async ngOnInit(): Promise<void> {
        this.artist = await this._artistService.fetchAndUpdateCurrent();
    }
}