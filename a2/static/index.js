'use strict';

const addFooterOnClickHandler = () => {
    window.document.getElementById('footer').addEventListener('click', () => {
        window.location = 'https://www.artsy.net';
    });
};

const addQuerySearchOnClickHandler = () => {
    window.document.getElementById('query-search').addEventListener('click', () => {
        window.document.getElementById('search').requestSubmit();
    });
};

window.addEventListener('load', () => {
    addFooterOnClickHandler();
    addQuerySearchOnClickHandler();
});
