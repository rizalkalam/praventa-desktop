# Praventa Desktop App

**Praventa** adalah aplikasi desktop berbasis JavaFX dengan struktur MVC, dilengkapi fitur login, navigasi sidebar, dan sistem manajemen halaman yang modular.

## 🧩 Fitur

- Autentikasi pengguna (Login)
- Navigasi sidebar dengan indikator aktif
- Manajemen halaman (Multi-view dengan FXML)
- Animasi transisi antar halaman
- Struktur proyek modular berbasis MVC

## 🛠️ Teknologi

- Java 17+
- JavaFX (FXML, Scene Builder)
- Maven (build & dependency)
- MVC pattern
- MySQL (opsional untuk login database)

## 🗂️ Struktur Folder

```bash
src/
└── main/
    ├── java/
    │   └── com/example/praventa/
    │       ├── Main.java
    │       ├── controller/
    │       ├── model/
    │       ├── repository/
    │       └── utils/
    └── resources/
        └── com/example/praventa/
            ├── fxml/
            └── assets/

🚀 Cara Menjalankan
Clone repository:

bash
Copy
Edit
git clone https://github.com/username/praventa-desktop.git
cd praventa-desktop
Import ke IDE (IntelliJ IDEA/VS Code/Eclipse)

Jalankan file Main.java (pastikan JavaFX SDK sudah disetting di IDE)

📸 Tampilan
(Opsional) Tambahkan screenshot tampilan UI login & sidebar

📄 Lisensi
Proyek ini dilisensikan di bawah MIT License.

✍️ Kontribusi
Kontribusi sangat terbuka! Silakan buat issue atau pull request.

yaml
Copy
Edit

---

### 🔧 Tips Tambahan
- Jika ingin membagikan project secara open-source, sebaiknya tambahkan juga file `LICENSE` (misalnya MIT atau Apache 2.0).
- Buat branch `dev` untuk pengembangan sebelum merge ke `main`.

Kalau kamu berencana menghosting ini di GitHub dan ingin juga CI/CD, bisa aku bantu tambahkan `.github/workflows` juga nanti.

Perlu dibuatkan langsung file `README.md`-nya atau ingin dibuat repo template GitHub secara lokal dulu?








