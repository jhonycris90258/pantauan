const express = require('express');
const app = express();
const PORT = process.env.PORT || 3000;

app.use(express.json());

let dataAnak = {
    lokasi: { lat: 0, lon: 0 },
    notifikasiWhatsApp: [],
    aplikasiTerinstal: [],
    waktuUpdate: '-'
};

app.post('/api/lapor', (req, res) => {
    const { lokasi, notifikasi, apps } = req.body;
    if (lokasi) dataAnak.lokasi = lokasi;
    if (notifikasi) dataAnak.notifikasiWhatsApp.push(notifikasi);
    if (apps) dataAnak.aplikasiTerinstal = apps;
    dataAnak.waktuUpdate = new Date().toLocaleString();

    res.json({ status: 'success', message: 'Data berhasil direkam' });
});

app.get('/api/pantau', (req, res) => {
    res.json(dataAnak);
});

app.listen(PORT, () => {
    console.log(`Server berjalan di port ${PORT}`);
});
