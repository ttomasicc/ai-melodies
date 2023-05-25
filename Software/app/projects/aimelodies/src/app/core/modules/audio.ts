export const load = (url: string): Promise<HTMLAudioElement> =>
    new Promise((resolve, reject) => {
        const audio = new Audio(url);
        audio.addEventListener('loadedmetadata', () => resolve(audio));
        audio.addEventListener('error', (err) => reject(err));
    });