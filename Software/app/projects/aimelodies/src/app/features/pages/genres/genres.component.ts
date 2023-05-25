import {Component, OnInit} from '@angular/core';
import {take} from 'rxjs';
import {Genre} from '../../../core/models/domain/genre';
import {GenreService} from '../../../core/services/genre.service';
import {GenresResponse} from '../../../core/models/responses/genres.response';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
    selector: 'app-genres',
    templateUrl: './genres.component.html',
    styleUrls: ['./genres.component.sass']
})
export class GenresComponent implements OnInit {
    allGenres: Genre[] = [];
    allGenresResponse?: GenresResponse;
    warningDialogOpen: boolean = false;
    addGenreDialogOpen: boolean = false;
    updateGenreDialogOpen: boolean = false;
    updateGenreObject!: Genre;
    searchTerm: string = '';

    constructor(private readonly _genreService: GenreService) {
    }

    ngOnInit(): void {
        this.getAllGenres();
    }

    openAddGenreDialog = (): boolean => {
        this.addGenreDialogOpen = true;
        return false;
    }

    closeAddGenreDialog = (): boolean => {
        this.addGenreDialogOpen = false;
        return false;
    }

    openUpdateGenreDialog = (genre: Genre): boolean => {
        this.updateGenreObject = {...genre};
        this.updateGenreDialogOpen = true;
        return false;
    }

    closeUpdateGenreDialog = (): boolean => {
        this.updateGenreDialogOpen = false;
        return false;
    }

    openWarningDialog = (): boolean => {
        this.warningDialogOpen = true;
        return false;
    }

    closeWarningDialog = (): boolean => {
        this.warningDialogOpen = false;
        return false;
    }

    clearSearch = (): void => {
        this.searchTerm = '';
    }

    insertGenre = (genre: Genre): void => {
        this.searchTerm = genre.name;
        const inx = this.allGenres.findIndex((it: Genre) => it.name > genre.name);

        if (inx === -1) {
            this.allGenres.push(genre);
        } else {
            this.allGenres.splice(inx, 0, genre);
        }

        this.closeAddGenreDialog();
    }

    updateGenre = (genre: Genre): void => {
        this.searchTerm = genre.name;
        const inx = this.allGenres.findIndex((it: Genre) => it.id === genre.id);
        this.allGenres[inx].name = genre.name;
        this.allGenres = this.allGenres.concat();

        this.closeUpdateGenreDialog();
    }

    deleteGenre = (genre: Genre): boolean => {
        this._genreService
            .delete(genre._links.self.href)
            .pipe(take(1))
            .subscribe({
                next: _ => this.removeGenre(genre),
                error: (err: HttpErrorResponse) => alert(err.error.message)
            });
        return false;
    }

    deleteUnusedGenres = (): void => {
        this.searchTerm = '';
        this._genreService
            .deleteUnused(this.allGenresResponse!._links.self.href)
            .pipe(take(1))
            .subscribe({
                next: _ => {
                    this.warningDialogOpen = false;
                    this.getAllGenres();
                }
            });
    }

    private removeGenre = (genre: Genre): void => {
        this.searchTerm = '';
        this.allGenres = this.allGenres.filter((it) => it.id !== genre.id);
    }

    private getAllGenres = (): void => {
        this._genreService.getAll()
            .pipe(take(1))
            .subscribe({
                next: (response) => {
                    this.allGenresResponse = response;
                    this.allGenres = response._embedded?.genres ?? [];
                }
            });
    }
}