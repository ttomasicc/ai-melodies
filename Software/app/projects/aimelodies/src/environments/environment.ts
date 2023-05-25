export const environment = {
    production: false,
    api: {
        host: 'localhost',
        port: 8080,
        baseUri: '/api/v1',
        resources: {
            melodies: '/static/melodies',
            albums: '/static/albums',
            artists: '/static/artists'
        }
    }
};