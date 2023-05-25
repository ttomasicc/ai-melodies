import {Component, HostListener, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Album} from '../../../core/models/domain/album';
import {AlbumService} from '../../../core/services/album.service';
import {take} from 'rxjs';
import {ArtistService} from '../../../core/services/artist.service';
import {Artist} from '../../../core/models/domain/artist';
import {Melody} from '../../../core/models/domain/melody';
import {MelodyService} from '../../../core/services/melody.service';
import * as audio from '../../../core/modules/audio';
import {MelodiesResponse} from '../../../core/models/responses/melodies.response';

@Component({
    selector: 'app-album-details',
    templateUrl: './album-details.component.html',
    styleUrls: ['./album-details.component.sass']
})
export class AlbumDetailsComponent implements OnInit {
    album?: Album;
    melodiesResponse?: MelodiesResponse;
    melodies: { melody: Melody, audioElement: HTMLAudioElement }[] = [];
    currentlyPlaying?: { melody: Melody, audioElement: HTMLAudioElement } = undefined;
    currentTime: number = 0;
    progressValue: number = 0;
    currentArtist?: Artist;

    detailsOpen: boolean = false;
    deleteMelodyWarningDialogOpen: boolean = false;
    addMelodyDialogOpen: boolean = false;
    editAlbumDialogOpen: boolean = false;
    deleteAlbumWarningDialogOpen: boolean = false;

    private albumHref: string = '';
    private melodyToDelete?: Melody;

    constructor(
        private readonly _router: Router,
        private readonly _albumService: AlbumService,
        private readonly _melodyService: MelodyService,
        private readonly _artistService: ArtistService
    ) {
        const state = this._router.getCurrentNavigation()?.extras.state;
        if (state) this.albumHref = state['albumHref'];
    }

    @HostListener('document:keydown.space', ['$event'])
    onSpaceDown(event: KeyboardEvent) {
        if (!(this.addMelodyDialogOpen || this.editAlbumDialogOpen)) {
            event.preventDefault();
            if (this.currentlyPlaying)
                this.currentlyPlaying.audioElement.paused ? this.continue() : this.pause();
        }
    }

    async ngOnInit(): Promise<void> {
        await this.loadAlbum();
        this.currentArtist = await this.getCurrentArtist();
    }

    clickDetails = (event: any): void => {
        event.preventDefault();
        this.detailsOpen = !this.detailsOpen;
    }

    navigateToArtist = (artistHref: string): boolean => {
        this._router.navigate(
            ['/artists'],
            {state: {artistHref}}
        ).then();
        return false;
    }

    play = (melodyId: number): boolean => {
        if (this.currentlyPlaying) {
            this.currentlyPlaying.audioElement.muted = false;
            this.pause();
            this.currentlyPlaying.audioElement.currentTime = 0;
        }

        const melody = this.melodies.find((it) => it.melody.id === melodyId);
        if (melody) {
            this.currentlyPlaying = melody;
            this.currentlyPlaying.audioElement.currentTime = 0;

            this.currentlyPlaying.audioElement.ontimeupdate = (e: any) => {
                this.currentTime = Math.trunc(e.target.currentTime);
                this.progressValue = (e.target.currentTime / e.target.duration) * 100;
                if (e.target.currentTime / e.target.duration === 1 && this.currentlyPlaying)
                    this.currentlyPlaying.audioElement.currentTime = 0;
            }

            this.currentlyPlaying.audioElement.play().then();
        }
        return false;
    }

    previousTrack = (): void => {
        if (this.currentlyPlaying) {
            const index = this.melodies.findIndex((it) => it.melody.id === this.currentlyPlaying?.melody.id);
            this.play(index === 0 ? this.melodies[this.melodies.length - 1].melody.id : this.melodies[index - 1].melody.id);
        }
    }

    nextTrack = (): void => {
        if (this.currentlyPlaying) {
            const index = this.melodies.findIndex((it) => it.melody.id === this.currentlyPlaying?.melody.id);
            this.play(index === this.melodies.length - 1 ? this.melodies[0].melody.id : this.melodies[index + 1].melody.id);
        }
    }

    playRandom = (): void => {
        if (this.melodies.length)
            this.play(this.melodies[~~(Math.random() * this.melodies.length)].melody.id)
    }

    pause = (): boolean => {
        this.currentlyPlaying?.audioElement?.pause();
        return false;
    }

    continue = (event: any | undefined = undefined): boolean => {
        if (event && this.currentlyPlaying)
            this.currentlyPlaying.audioElement.currentTime =
                (event.target.value / 100) * this.currentlyPlaying.audioElement.duration;
        this.currentlyPlaying?.audioElement?.play().then();
        return false
    }

    switchVolume = (): boolean => {
        if (this.currentlyPlaying)
            this.currentlyPlaying.audioElement.muted = !this.currentlyPlaying.audioElement.muted;
        return false;
    }

    openAddMelodyDialog = (): void => {
        this.addMelodyDialogOpen = true;
    }

    closeAddMelodyDialog = (): void => {
        this.addMelodyDialogOpen = false;
    }

    deleteMelody = (melody: Melody): boolean => {
        this.melodyToDelete = melody;
        this.deleteMelodyWarningDialogOpen = true;
        return false;
    }

    closeDeleteMelodyWarningDialog = () => {
        this.melodyToDelete = undefined;
        this.deleteMelodyWarningDialogOpen = false;
    }

    deleteSelectedMelody = (): void => {
        if (this.melodyToDelete) {
            this._melodyService
                .delete(this.melodyToDelete._links.self.href)
                .pipe(take(1))
                .subscribe({
                    next: _ => {
                        this.melodies = this.melodies.filter((it) => it.melody.id !== this.melodyToDelete!!.id);
                        this.album!!.melodies = this.album!!.melodies!!.filter((it) => it.id !== this.melodyToDelete!!.id);

                        if (this.currentlyPlaying?.melody?.id === this.melodyToDelete!!.id) {
                            this.pause();
                            this.currentlyPlaying.melody.name = '';
                            this.currentlyPlaying.melody.duration = 0;
                            this.currentlyPlaying.audioElement.currentTime = 0;
                        }

                        this.closeDeleteMelodyWarningDialog();
                    }
                });
        } else {
            this.closeDeleteMelodyWarningDialog();
        }
    }

    openEditAlbumDialog = (): void => {
        this.editAlbumDialogOpen = true;
    }

    closeEditAlbumDialog = (): void => {
        this.editAlbumDialogOpen = false;
    }

    openDeleteAlbumWarningDialog = (): void => {
        this.deleteAlbumWarningDialogOpen = true;
    }

    closeDeleteAlbumWarningDialog = (): void => {
        this.deleteAlbumWarningDialogOpen = false;
    }

    deleteAlbum = (): void => {
        this._albumService
            .delete(this.album?._links.self.href ?? '')
            .pipe(take(1))
            .subscribe({
                next: _ => this._router.navigate(['/my-albums']).then()
            });
    }

    insertMelody = async (melody: Melody) => {
        const [melodyWithDuration] = await this.loadMelodies([melody]);
        this.album?.melodies?.unshift(melodyWithDuration);
        this.closeAddMelodyDialog();
    }

    loadAlbum = async (album: Album | undefined = undefined): Promise<void> => {
        this.album = await this.getAlbum(album);
        this.closeEditAlbumDialog();
    }

    private getAlbum = (album: Album | undefined = undefined): Promise<Album> =>
        new Promise((resolve, reject) => {
            this._albumService
                .get(album ? album._links.self.href : this.albumHref)
                .pipe(take(1))
                .subscribe({
                    next: async (album: Album) => {
                        album.artist = await this.getArtist(album);
                        album.melodies = (await this.getMelodies(album)).sort((a, b) =>
                            new Date(b.dateCreated).getTime() - new Date(a.dateCreated).getTime()
                        );
                        resolve(album);
                    },
                    error: (err) => reject(err)
                });
        });

    private getArtist = (album: Album): Promise<Artist> =>
        new Promise((resolve, reject) => {
            this._artistService
                .get(album._links.artist.href)
                .pipe(take(1))
                .subscribe({
                    next: (artist) => resolve(artist as Artist),
                    error: (err) => reject(err)
                });
        });

    private getMelodies = (album: Album): Promise<Melody[]> =>
        new Promise((resolve, reject) => {
            this._melodyService
                .getAll(album._links.melodies.href)
                .pipe(take(1))
                .subscribe({
                    next: async (melodiesResponse) => {
                        this.melodiesResponse = melodiesResponse;
                        resolve(
                            await this.loadMelodies(melodiesResponse._embedded?.melodies ?? [])
                        );
                    },
                    error: (err) => reject(err)
                })
        });

    private loadMelodies = async (melodies: Melody[]): Promise<Melody[]> => {
        const fetchedMelodies = await Promise.all(
            melodies.map(async (melody) => {
                const audioElement = await audio.load(melody.audio);
                const melodyWithDuration = {
                    ...melody,
                    duration: Math.trunc(audioElement.duration)
                };

                this.melodies.push({melody: melodyWithDuration, audioElement});
                return melodyWithDuration;
            })
        );

        this.melodies.sort((a, b) =>
            new Date(b.melody.dateCreated).getTime() - new Date(a.melody.dateCreated).getTime()
        );

        return fetchedMelodies;
    }

    private getCurrentArtist = (): Promise<Artist | undefined> =>
        new Promise((resolve) => {
            this._artistService.getCurrent()
                .pipe(take(1))
                .subscribe({
                    next: (artist) => resolve(artist)
                })
        });
}